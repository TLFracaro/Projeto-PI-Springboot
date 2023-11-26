import Cabecalho2 from "../../components/Cabecalho2";
import "./index.scss";
import { Link, useLocation } from "react-router-dom";

import '../../css/global.css';
import Rodape from "../../components/Rodape";
import { useEffect, useState } from "react";

export default function VizualizarProdutos() {
    const location = useLocation();
    const produtoId = location.state || {};
    const [produto, setProduto] = useState({});
    const [imagensUrl, setImagensUrl] = useState([]);

    const token = localStorage.getItem('token');
    const decodedToken = JSON.parse(atob(token.split('.')[1]));

    useEffect(() => {
        async function fetchData() {
            try {
                const response = await fetch(`http://localhost:8080/produto?produtoId=${produtoId}`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`
                    },
                });

                const responseData = await response.json();

                const imagens = responseData.imagens;

                console.log('Dados do Produto:', responseData);
                console.log('Imagens:', imagens);

                setProduto(responseData);

                const imagensUrl = imagens.map((imagem) => {
                    return imagem.url;
                });

                setImagensUrl(imagensUrl);
            } catch (error) {
                console.error(error);
            }
        }

        fetchData();
    }, [produtoId, token]);



    const calcularGrid = () => {
        const numeroDeImagens = imagensUrl.length;
        const colunas = numeroDeImagens === 4 ? 2 : 1;
        const linhas = Math.ceil(numeroDeImagens / colunas);

        return {
            gridTemplateColumns: `repeat(${colunas}, 1fr)`,
            gridTemplateRows: `repeat(${linhas}, 1fr)`,
        };
    };

    const dataDeInclusao = new Date(produto.dataDeInclusao);

    const addZero = (num) => (num < 10 ? `0${num}` : num);

    const dia = addZero(dataDeInclusao.getDate());
    const mes = addZero(dataDeInclusao.getMonth() + 1);
    const ano = dataDeInclusao.getFullYear();
    const horas = addZero(dataDeInclusao.getHours());
    const minutos = addZero(dataDeInclusao.getMinutes());

    return (
        <section className="VizualizarProdutoEstilo">
            
            <Cabecalho2 tipoPrivilegio={decodedToken.privilegio} />

            <main>
                <div class="mainConteudo">
                    <div class="voltar">
                        <Link to="/produtos">
                            <h1><svg width="48" height="48" viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path d="M23.3794 31.5L15.9375 24L23.3794 16.5M16.9716 24H32.0625" stroke="black"
                                    stroke-width="4" stroke-linecap="round" stroke-linejoin="round" />
                                <path
                                    d="M42 24C42 14.0625 33.9375 6 24 6C14.0625 6 6 14.0625 6 24C6 33.9375 14.0625 42 24 42C33.9375 42 42 33.9375 42 24Z"
                                    stroke="black" stroke-width="4" stroke-miterlimit="10" />
                            </svg>
                                Voltar</h1>
                        </Link>
                    </div>
                    <h1 id="titulo">• Informações do produto:</h1>
                    <div class="conteudo">
                        <div className="imagens" style={calcularGrid()}>
                            {imagensUrl.map((imagem, index) => (
                                <div key={index}>
                                    <img
                                        src={imagem}
                                        alt={`Imagem ${index + 1}`}
                                        style={{ objectFit: 'cover' }}
                                    />
                                </div>
                            ))}
                        </div>

                        <div class="infos">
                            <div>
                                <h4>Nome:⠀<p>{produto.nome}</p></h4>
                            </div>
                            <h4>Categoria:⠀<p>{produto.categoria}</p></h4>
                            <h4>Marca:⠀<p>{produto.marca}</p></h4>
                            <h4>Preço:⠀<p>R$ {produto.preco}</p></h4>
                            <h4>Descrição do produto:⠀<p>{produto.descricao}</p></h4>
                            <h4>Localização no estoque:⠀<p>{produto.loc_estoque}</p></h4>
                            <div class="variacoes">
                                <h4>Variações:</h4>
                                <table>
                                    <tr>
                                        <th>Tamanho</th>
                                        <th>Cor</th>
                                        <th>Quantidade</th>
                                    </tr>
                                    {produto.variacoes?.map((variacao) => (
                                        <tr class='Conteudo' key={variacao.variacaoId}>
                                            <td class="primeiro">Tamanho: {variacao.tamanho}</td>
                                            <td>Cor: {variacao.cor}</td>
                                            <td class="final">Quantidade: {variacao.quantidade}</td>
                                        </tr>
                                    ))}
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </main>

            <Rodape />

        </section>
    );
}