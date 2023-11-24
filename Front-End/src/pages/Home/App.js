import "./App.scss"
import cartaz1 from '../../assets/image/cartaz1.svg';
import Cabecalho1 from "../../components/Cabecalho1";
import Rodape from "../../components/Rodape";

import '../../css/global.css';
import { useEffect, useState } from "react";

export default function Home() {
    const [produto, setProduto] = useState([]);

    return (
        <section class="HomeEstilo">

            <Cabecalho1 />

            <main>
                <div class='conteudoMain'>
                    <div class='faixa1'>
                        <img src={cartaz1} alt="" />
                    </div>
                    <div class='produtosExibir'>
                        <h2>Nossos produtos:</h2>

                    </div>
                </div>
            </main>

            <Rodape />

        </section>
    );
}