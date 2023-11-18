package com.projeto_pi.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import com.projeto_pi.dtos.ProdutoDto;
import com.projeto_pi.models.Imagem;
import com.projeto_pi.models.Produto;
import com.projeto_pi.models.Variacao;
import com.projeto_pi.repositories.ImagemRepository;
import com.projeto_pi.repositories.ProdutoRepository;

import jakarta.transaction.Transactional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ImagemRepository imagemRepository;

    @Autowired
    private ImagesService service;

    public List<Produto> selectAll() throws Exception {
        var produtos = produtoRepository.findAll();
        if (produtos.isEmpty()) {
            throw new NoSuchElementException("Não há produtos cadastrados");
        }
        return produtos;
    }

    public Produto selectOne(UUID produtoId) throws Exception {
        return produtoRepository
                .findById(produtoId)
                .orElseThrow(() -> new NoSuchElementException("Produto não encontrado"));
    }

    @Transactional
    public Produto insert(ProdutoDto dto) throws Exception {
        Produto produto = new Produto();
        BeanUtils.copyProperties(dto, produto);

        produtoRepository.findOne(Example.of(produto, ExampleMatcher
                .matching()
                .withIgnorePaths("produto_id", "variacoes", "imagens")))
                .ifPresent(p -> {
                    throw new IllegalArgumentException("Produto já cadastrado");
                });

        ExecutorService executor = Executors.newFixedThreadPool(2);

        var threadImagens = executor.submit(() -> dto.imagens()
                .stream()
                .map(imagem -> {
                    String str = service.checkIfImageExist(imagem.getBase64());
                    if (str != null) {
                        imagem.setBase64(str);
                    }
                    return imagem;
                })
                .map(imagem -> {
                    Imagem img = new Imagem();
                    img.setProduto(produto);
                    img.setUrl(service.uploadImage(imagem));
                    return img;
                })
                .collect(Collectors.toList()));

        var threadVariacoes = executor.submit(() -> dto.variacoes()
                .stream()
                .map(variacaoDto -> {
                    Variacao variacao = new Variacao();
                    BeanUtils.copyProperties(variacaoDto, variacao);
                    variacao.setProduto(produto);
                    return variacao;
                })
                .collect(Collectors.toList()));

        produto.setImagens(threadImagens.get());
        produto.setVariacoes(threadVariacoes.get());

        executor.shutdown();

        return produtoRepository.save(produto);
    }

    @Transactional
    public Produto update(UUID produtoId, ProdutoDto dto) throws Exception {

        Produto produto = produtoRepository
                .findById(produtoId)
                .orElseThrow(() -> new NoSuchElementException("Produto não encontrado"));

        BeanUtils.copyProperties(dto, produto);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        var threadImagens = executor.submit(() -> dto.imagens()
                .stream()
                .map(imagem -> {
                    String str = service.checkIfImageExist(imagem.getBase64());
                    if (str != null) {
                        imagem.setBase64(str);
                    }
                    return imagem;
                })
                .map(imagem -> {
                    Imagem img = new Imagem();
                    img.setProduto(produto);
                    img.setUrl(service.uploadImage(imagem));
                    return img;
                })
                .collect(Collectors.toList()));

        var threadVariacoes = executor.submit(() -> dto.variacoes()
                .stream()
                .map(variacaoDto -> {
                    Variacao variacao = new Variacao();
                    BeanUtils.copyProperties(variacaoDto, variacao);
                    variacao.setProduto(produto);
                    return variacao;
                })
                .collect(Collectors.toList()));

        Set<Imagem> imagemSet = new HashSet<>(produto.getImagens());
        Set<Variacao> variacaoSet = new HashSet<>(produto.getVariacoes());

        imagemSet.addAll(threadImagens.get());
        variacaoSet.addAll(threadVariacoes.get());

        produto.getImagens().clear();
        produto.getImagens().addAll(imagemSet);
        
        produto.getVariacoes().clear();
        produto.getVariacoes().addAll(variacaoSet);

        executor.shutdown();

        return produtoRepository.save(produto);
    }

    @Transactional
    public Boolean delete(UUID produtoId) throws Exception {
        produtoRepository.findById(produtoId).ifPresentOrElse(produto -> {

            produtoRepository.deleteById(produtoId);
            imagemRepository.deleteAllByProdutoId(produtoId);

            produto.getImagens().forEach(imagem -> {

                Imagem imagemToExample = new Imagem();
                imagemToExample.setUrl(imagem.getUrl());

                Long count = imagemRepository.count(Example.of(imagemToExample, ExampleMatcher
                        .matching()
                        .withIgnorePaths("imagem_id", "produto_id")
                        .withMatcher("url", ExampleMatcher.GenericPropertyMatcher
                                .of(ExampleMatcher.StringMatcher.EXACT))));

                if (count == 0) {
                    try (ExecutorService executor = Executors.newFixedThreadPool(2)) {

                        List<Callable<Boolean>> callables = new ArrayList<>();

                        callables.add(() -> service.deleteJsonReference(imagem.getUrl()));
                        callables.add(() -> service.deleteImage(imagem.getUrl()));

                        var results = executor.invokeAll(callables);

                        results.forEach(result -> {
                            try {
                                result.get();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        });
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

            });
        },
                () -> {
                    throw new NoSuchElementException("Produto não encontrado");
                });

        return true;
    }
}
