import axios from 'axios';
import { useEffect, useRef, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Cabecalho1 from '../../components/Cabecalho1';
import Rodape from '../../components/Rodape';
import '../../css/global.css';
import './index.scss';
import api from '../../api';

export default function Login() {
    const [email, setEmail] = useState('');
    const [senha, setSenha] = useState('');
    const navigate = useNavigate();
    const [texto, setTexto] = useState('');
    const [modalAberto, setModalAberto] = useState(false);

    const caixaDeDialogo = useRef(null);

    useEffect(() => {
        caixaDeDialogo.current = document.getElementById("CaixaDeDialogo");
    }, []);

    const fecharModal = () => {
        if (caixaDeDialogo.current) {
            caixaDeDialogo.current.close();
        }
    };

    const mostrarModal = () => {
        if (caixaDeDialogo.current) {
            caixaDeDialogo.current.showModal();
        }
    };


    const login = async () => {
        try {
            const json = {
                email: email,
                senha: senha
            };
    
            const response = await fetch('http://localhost:8080/auth/login', {
                method: 'POST',
                headers: {
                    'Content-type': 'application/json'
                },
                body: JSON.stringify(json),
            });
    
            const responseData = await response.json();
    
            console.log(responseData);
    
            if (response.status === 200) {
                const data = responseData.data;
                console.log(response.status);
                console.log(data);
                setTexto("Login efetuado com sucesso!")
                mostrarModal()
                setTimeout(() => {
                    navigate('/menuadm');
                }, 1000);
            } else {
                setTexto(responseData.error || 'Ocorreu um erro ao realizar login.');
                mostrarModal();
            }
        } catch (error) {
            console.error(error);
            setTexto('Ocorreu um erro ao realizar login.');
            mostrarModal();
        }
    };
    


    const enviar = (e) => {
        e.preventDefault();
        login();
    };

    const ApertaEnter = (e) => {
        if (e.key === 'Enter') {
            e.preventDefault();
            login();
        }
    };

    useEffect(() => {
        document.addEventListener('keypress', ApertaEnter);
        return () => {
            document.removeEventListener('keypress', ApertaEnter);
        };
    }, [email, senha]);

    return (
        <section className="LoginEstilo">
            <Cabecalho1 />

            <main>
                <section className="conteudoMain">
                    <dialog open={modalAberto} id="CaixaDeDialogo">
                        <p>{texto}</p>
                        <button id="botao" onClick={fecharModal}>
                            Ok
                        </button>
                    </dialog>
                    <div className="imagem">
                        <img
                            src="/assets/image/imagemLogin.svg"
                            alt="Uma imagem representando o cliente em um catálogo de loja de roupa"
                        />
                    </div>
                    <div className="areaLogin">
                        <div className="loginTexto">
                            <h1>LOGIN</h1>
                            <h2>BEM VINDO DE VOLTA!</h2>
                            <img src="/assets/image/linhaLogin.svg" alt="Linha separando caixas de texto do título" />
                        </div>
                        <form onSubmit={enviar}>
                            <label htmlFor="email">e-mail:</label>
                            <input id="email" type="text" value={email} onChange={(e) => setEmail(e.target.value)} />
                            <label htmlFor="senha">senha:</label>
                            <input id="senha" type="password" value={senha} onChange={(e) => setSenha(e.target.value)} />
                            <Link to="/">Esqueceu sua senha?</Link>
                            <button type="submit"> Login </button>
                        </form>
                        <div className="criarConta">
                            <p>
                                Ainda não tem conta?<Link to="/cadastro">Clique aqui</Link>
                            </p>
                        </div>
                    </div>
                </section>
            </main>

            <Rodape />
        </section>
    );
}
