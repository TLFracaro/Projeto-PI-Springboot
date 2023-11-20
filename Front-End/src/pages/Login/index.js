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
            const formData = new FormData();
            formData.append('email', email);
            formData.append('senha', senha);
            
            const token = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJ0aGlua3NoYXJlYXBpIiwic3ViIjoidHVsaW9AZ21haWwuY29tIiwiZXhwIjoxNzAwNTEzMTM5LCJwcml2aWxlZ2lvIjoiQURNSU4iLCJ1c3VhcmlvSWQiOiJkZjczMDVkZC0yODJlLTRlZGQtYmE2Ny1hNzE0MTc4MWIxOGYifQ.TyRBIAE_UfJToAGNM2KfL2oPpnV_-Z9gINhQ1C7WaBs';

            /* =============== AQUI =============== */

            const r = await fetch('http://localhost:8080/auth/login', {
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiVVNVQVJJTyIsIklzc3VlciI6ImVjb21tZXJjZSIsIlVzZXJuYW1lIjoidGVzdGUiLCJleHAiOjE2OTk5MDk5OTcsImlhdCI6MTY5OTkwOTk5N30.Ua4Hk1F5PulvwqlsZkja48PMO0NTbkUXp_xfYELka74'
                },
                body: formData,
            });
  
            /* ==================================== */

            const data = await r.json();

            if (r && r.data && r.data.nome && r.data.cpf && r.data.email && r.data.privilegio) {
                const { nome, cpf, email, privilegio } = r.data;
                console.log({ nome, cpf, email, privilegio });
                navigate('/menuadm', { state: { nome, cpf, email, privilegio } });
            } else {
                setTexto('O usuário não existe!');
                mostrarModal();
            }
        } catch (error) {
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
