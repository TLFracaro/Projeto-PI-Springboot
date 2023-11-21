package com.projeto_pi.services;

import com.projeto_pi.dtos.ProdutoDto;
import com.projeto_pi.models.Imagem;
import com.projeto_pi.models.Produto;
import com.projeto_pi.models.Variacao;
import com.projeto_pi.repositories.ImagemRepository;
import com.projeto_pi.repositories.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    private final ImagemRepository imagemRepository;

    private final ImagesService service;

    public ProdutoService(ProdutoRepository produtoRepository, ImagemRepository imagemRepository, ImagesService service) {
        this.produtoRepository = produtoRepository;
        this.imagemRepository = imagemRepository;
        this.service = service;
    }

    public List<Produto> selectAll() {
        var produtos = produtoRepository.findAll();
        if (produtos.isEmpty()) {
            throw new NoSuchElementException("Não há produtos cadastrados");
        }
        return produtos;
    }

    public Produto selectOne(UUID produtoId) {
        return produtoRepository
                .findById(produtoId)
                .orElseThrow(() -> new NoSuchElementException("Produto não encontrado"));
    }

    @Transactional
    public Produto insert(ProdutoDto dto, MultipartFile[] imagens) throws Exception {
        Produto produto = new Produto();
        BeanUtils.copyProperties(dto, produto);

        produtoRepository.findOne(Example.of(produto, ExampleMatcher
                        .matching()
                        .withIgnorePaths("produto_id", "variacoes", "imagens")))
                .ifPresent(p -> {
                    throw new IllegalArgumentException("Produto já cadastrado");
                });

        var future = CompletableFuture.supplyAsync(() -> dto.variacoes()
                .stream()
                .map(variacaoDto -> {
                    Variacao variacao = new Variacao();
                    BeanUtils.copyProperties(variacaoDto, variacao);
                    variacao.setProduto(produto);
                    return variacao;
                })
                .collect(Collectors.toList()));

        List<Imagem> imgs = new ArrayList<>();

        Arrays.stream(imagens)
                .forEach(imagem -> {
                    String filename = service.checkIfImageExist(imagem);
                    Imagem img = new Imagem();
                    img.setProduto(produto);
                    img.setUrl(filename == null ? service.uploadImage(imagem) : filename);
                    imgs.add(img);
                });

        produto.setImagens(imgs);
        produto.setVariacoes(future.get());

        return produtoRepository.save(produto);
    }

    @Transactional
    public Produto update(UUID produtoId, ProdutoDto dto, MultipartFile[] imagens) throws Exception {

        Produto produto = produtoRepository
                .findById(produtoId)
                .orElseThrow(() -> new NoSuchElementException("Produto não encontrado"));

        BeanUtils.copyProperties(dto, produto);

        var future = CompletableFuture.supplyAsync(() -> dto.variacoes()
                .stream()
                .map(variacaoDto -> {
                    Variacao variacao = new Variacao();
                    BeanUtils.copyProperties(variacaoDto, variacao);
                    variacao.setProduto(produto);
                    return variacao;
                })
                .collect(Collectors.toList()));

        Set<Imagem> imagemSet = new HashSet<>(produto.getImagens());

        Arrays.stream(imagens)
                .forEach(imagem -> {
                    String filename = service.checkIfImageExist(imagem);
                    Imagem img = new Imagem();
                    img.setUrl(filename == null ? service.uploadImage(imagem) : filename);
                    imagemSet.add(img);
                });

        Set<Variacao> variacaoSet = new HashSet<>(produto.getVariacoes());

        variacaoSet.addAll(future.get());

        produto.getImagens().clear();
        produto.getImagens().addAll(imagemSet);

        produto.getVariacoes().clear();
        produto.getVariacoes().addAll(variacaoSet);

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

                        long count = imagemRepository.count(Example.of(imagemToExample, ExampleMatcher
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
