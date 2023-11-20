import "./index.scss";
import { useLocation } from 'react-router-dom';
import React, { useEffect, useState } from 'react';
import gatoToca from '../../assets/image/gatoToca.jpg';
import Cabecalho2 from "../../components/Cabecalho2";

import '../../css/global.css';
import Rodape from "../../components/Rodape";

export default function MenuADM() {
    const location = useLocation();
    const { nome, cpf, email } = location.state || {};

    useEffect(() => {
        localStorage.setItem("userData", JSON.stringify({ nome, cpf, email }));
    }, [nome, cpf, email]);

    const [storedUserData, setStoredUserData] = useState(null);

    useEffect(() => {
        const storedUserDataString = localStorage.getItem("userData");
        if (storedUserDataString) {
          const parsedUserData = JSON.parse(storedUserDataString);
          console.log("Dados recuperados:", parsedUserData);
          setStoredUserData(parsedUserData);
        }
      }, []);

    return (
        <section className="MenuAdmEstilo">

            <Cabecalho2 />

            <main>
                <div className="mainConteudo">
                    <div className="titulo">
                        <h1>Página inicial</h1>
                    </div>
                    <div className="conteudo">
                        <div className="dados">
                            <img src={gatoToca} alt="Sua imamgem"></img>
                            <div className="infosUser">
                                <h3>Dados Usuario:</h3>
                                <p>Nome: {storedUserData?.nome}</p>
                                <p>CPF: {storedUserData?.cpf}</p>
                                <p>E-mail: {storedUserData?.email}</p>
                            </div>
                        </div>
                        <div className="opcos">
                            <h1>Seja bem vindo&#40;a&#41;!</h1>
                            <h2>• Atalho para ferramentas:</h2>
                            <button><svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path d="M10.5019 1.85H10.5014H10.5C5.72303 1.85 1.85 5.72303 1.85 10.5C1.85 15.277 5.72303 19.15 10.5 19.15C15.277 19.15 19.15 15.277 19.15 10.5L19.15 10.4995C19.1356 5.72862 15.2714 1.86442 10.5019 1.85ZM11.075 6.25V6.1H10.925H9.65H9.5V6.25V11.35V11.4338L9.57139 11.4777L13.9914 14.1978L14.1191 14.2764L14.1978 14.1486L14.8777 13.0436L14.9573 12.9144L14.8272 12.8364L11.075 10.5851V6.25ZM10.5 17.15C6.82726 17.15 3.85 14.1727 3.85 10.5C3.85 6.82726 6.82726 3.85 10.5 3.85C14.1727 3.85 17.1499 6.82718 17.15 10.4999C17.1387 14.1685 14.1684 17.1387 10.5013 17.15C10.5012 17.15 10.5011 17.15 10.501 17.15L10.5 17.15Z" fill="black" stroke="black" stroke-width="0.3" />
                            </svg>
                                Ultimas compras</button>
                            <button><svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path d="M12.3251 2.61275C11.7497 2.85448 11.2278 3.20761 10.7894 3.65182C10.7893 3.65193 10.7892 3.65204 10.7891 3.65215L10.01 4.43121L9.21096 3.63215C9.21084 3.63203 9.21071 3.63191 9.21059 3.63178C8.77222 3.18759 8.25033 2.83447 7.67495 2.59275C7.09942 2.35096 6.48175 2.22541 5.85749 2.22332C5.23324 2.22123 4.61473 2.34264 4.03759 2.58057C3.46045 2.81849 2.93608 3.16823 2.49466 3.60965C2.05324 4.05107 1.7035 4.57544 1.46558 5.15258C1.22765 5.72972 1.10624 6.34823 1.10833 6.97248C1.11042 7.59674 1.23597 8.21441 1.47776 8.78995C1.71949 9.36533 2.07262 9.88723 2.51683 10.3256C2.51694 10.3257 2.51705 10.3258 2.51716 10.3259L9.88228 17.6911L9.98832 17.7971L9.98999 17.7954L10.0117 17.8171L10.1177 17.7111L17.4829 10.3459C17.483 10.3459 17.483 10.3458 17.4831 10.3457C17.9274 9.9073 18.2805 9.38537 18.5223 8.80995C18.7641 8.23441 18.8896 7.61674 18.8917 6.99248C18.8938 6.36823 18.7724 5.74972 18.5345 5.17258C18.2965 4.59544 17.9468 4.07107 17.5054 3.62965C17.064 3.18823 16.5396 2.83849 15.9624 2.60057C15.3853 2.36264 14.7668 2.24123 14.1425 2.24332C13.5183 2.24541 12.9006 2.37096 12.3251 2.61275ZM5.85282 10.8782L5.85198 10.8807L3.90691 8.93561C3.38804 8.41673 3.09653 7.71298 3.09653 6.97917C3.09653 6.24537 3.38804 5.54162 3.90691 5.02274C4.42579 4.50386 5.12954 4.21236 5.86335 4.21236C6.59715 4.21236 7.3009 4.50386 7.81978 5.02274L7.81983 5.02278L9.89895 7.10024L9.89899 7.10028L9.90478 7.10607L10.0109 7.21216L10.1169 7.10605L12.1802 5.0419C12.6991 4.52303 13.4029 4.23152 14.1367 4.23152C14.8705 4.23152 15.5742 4.52303 16.0931 5.0419C16.612 5.56078 16.9035 6.26453 16.9035 6.99834C16.9035 7.73212 16.612 8.43585 16.0932 8.95472C16.0931 8.95474 16.0931 8.95476 16.0931 8.95477L15.2513 9.79584H15.2511L15.2075 9.83793L13.9992 11.0046L13.9991 11.0046L13.9973 11.0065L9.98959 15.0149L6.02442 11.0498L5.85282 10.8782Z" fill="black" stroke="black" stroke-width="0.3" />
                            </svg>
                                Favoritos</button>
                            <button><svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path d="M15.6667 3.66663H4.83333C4.3731 3.66663 4 4.03972 4 4.49996V17.8333C4 18.2935 4.3731 18.6666 4.83333 18.6666H15.6667C16.1269 18.6666 16.5 18.2935 16.5 17.8333V4.49996C16.5 4.03972 16.1269 3.66663 15.6667 3.66663Z" stroke="black" stroke-width="2" stroke-linejoin="round" />
                                <path d="M7.75008 2V4.5M12.7501 2V4.5M6.91675 8.25H13.5834M6.91675 11.5833H11.9167M6.91675 14.9167H10.2501" stroke="black" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
                            </svg>
                                Pedidos</button>
                            <button><svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path d="M6.99988 18.7667C6.51484 18.7666 6.09701 18.5926 5.75179 18.2474C5.40653 17.9021 5.23275 17.4845 5.23333 16.9999M6.99988 18.7667L7.00012 15.2333C6.51551 15.2327 6.09788 15.4065 5.75262 15.7518C5.4074 16.097 5.23336 16.5148 5.23333 16.9999M6.99988 18.7667C6.99992 18.7667 6.99996 18.7667 7 18.7667V18.6667C7.45778 18.6672 7.85 18.5042 8.17667 18.1775C8.50333 17.8508 8.66667 17.4583 8.66667 17C8.66722 16.5422 8.50417 16.15 8.1775 15.8233C7.85083 15.4967 7.45833 15.3333 7 15.3333C6.54222 15.3328 6.15 15.4958 5.82333 15.8225C5.49667 16.1492 5.33333 16.5417 5.33333 17M6.99988 18.7667L5.33333 17M5.23333 16.9999L5.33333 17M5.23333 16.9999C5.23333 16.9999 5.23333 17 5.23333 17H5.33333M15.3332 18.7667C14.8482 18.7666 14.4303 18.5926 14.0851 18.2474C13.7399 17.9021 13.5661 17.4845 13.5667 16.9999M15.3332 18.7667L17.1 17.0001C17.1006 16.5155 16.9268 16.0979 16.5815 15.7526C16.2363 15.4074 15.8185 15.2334 15.3335 15.2333M15.3332 18.7667C15.3333 18.7667 15.3333 18.7667 15.3333 18.7667V18.6667C15.7911 18.6672 16.1833 18.5042 16.51 18.1775C16.8367 17.8508 17 17.4583 17 17C17.0006 16.5422 16.8375 16.15 16.5108 15.8233C16.1842 15.4967 15.7917 15.3333 15.3333 15.3333M15.3332 18.7667L15.3333 15.3333M13.5667 16.9999L13.6667 17H13.5667C13.5667 17 13.5667 16.9999 13.5667 16.9999ZM13.5667 16.9999C13.5667 16.5148 13.7407 16.097 14.086 15.7518C14.4312 15.4065 14.8488 15.2327 15.3335 15.2333M15.3335 15.2333L15.3333 15.3333M15.3335 15.2333C15.3334 15.2333 15.3334 15.2333 15.3333 15.2333V15.3333M17 12.7333H7.16913L7.97579 11.2667H14.125C14.4476 11.2667 14.7464 11.1856 15.0194 11.0235C15.2928 10.8611 15.51 10.6324 15.6708 10.3401C15.6709 10.34 15.6709 10.3399 15.671 10.3399L18.6291 5.00684L18.6291 5.00685L18.6302 5.0048C18.7991 4.683 18.7911 4.3583 18.6072 4.04369C18.4233 3.72928 18.1481 3.56667 17.7917 3.56667H5.56321L4.79866 1.95709L4.77154 1.9H4.70833H2H1.9V2V3.66667V3.76667H2H3.60338L6.55439 9.99656L5.45408 11.9934L5.45364 11.9942C5.1448 12.5672 5.16066 13.1492 5.4969 13.727C5.8339 14.306 6.33869 14.6 7 14.6H17H17.1V14.5V12.8333V12.7333H17ZM8.35459 9.4L6.45059 5.43333H16.2475L14.0659 9.4H8.35459Z" fill="black" stroke="black" stroke-width="0.2" />
                            </svg>

                                Carrinho</button>
                        </div>
                    </div>
                </div>
            </main>

            <Rodape />
        </section>
    );
}