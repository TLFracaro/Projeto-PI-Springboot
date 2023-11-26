import { BrowserRouter, Routes, Route, useNavigate, Navigate } from 'react-router-dom';

import App from "./pages/Home/App.js";
import Cadastro from "./pages/Cadastro/index.js";
import CadastroDeProdutos from "./pages/CadastroDeProdutos/index.js";
import GerenciamentoUsuario from "./pages/GerenciamentoUsuario/index.js";
import Menu from "./pages/Menu/index.js";
import Produtos from "./pages/Produtos/index.js";
import VizualizarProdutos from "./pages/VizualizarProdutos/index.js";
import Login from './pages/Login/index.js';
import Alterar from './pages/AlterarProduto/index.js';

export default function Rotas() {
    

    const isUserAdmin = () => { 
        const token = localStorage.getItem('token');
        if (token) {
            const decodedToken = JSON.parse(atob(token.split('.')[1]));
            const privilegio = decodedToken.privilegio;
            return privilegio === 'ADMIN';
        }
        return false;
    };

    const AdminRoute = ({ children }) => {
        if (!isUserAdmin()) {
            return <Navigate to="/login" />;
        }
        return children;
    };

    const PrivateRoute = ({ children }) => {
        const token = localStorage.getItem('token');
        if (!token) {
            return <Navigate to="/login" />;
        }
        return children;
    };

    return (
        <BrowserRouter>
            <Routes>
                <Route path='/' element={<App />} />
                <Route path="/cadastro" element={<Cadastro />} />
                <Route path="/login" element={<Login />} />
                <Route path="/cadastrodeprodutos" element={<AdminRoute><CadastroDeProdutos /></AdminRoute>} />
                <Route path="/gerenciamentousuario" element={<AdminRoute><GerenciamentoUsuario /></AdminRoute>} />
                <Route path="/menu" element={<PrivateRoute><Menu /></PrivateRoute>} />
                <Route path="/produtos" element={<AdminRoute><Produtos /></AdminRoute>} />
                <Route path="/vizualizarprodutos" element={<AdminRoute><VizualizarProdutos /></AdminRoute>} />
                <Route path="/alterarproduto" element={<AdminRoute><Alterar /></AdminRoute>} />
            </Routes>
        </BrowserRouter>
    );
}

