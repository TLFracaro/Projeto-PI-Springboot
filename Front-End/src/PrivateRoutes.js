import React from 'react';
import { Route, Navigate } from 'react-router-dom';

const PrivateRoute = ({ element: Element, ...rest }) => {
    const token = localStorage.getItem('token');
    const isAuthenticated = !!token;

    return (
        <React.Fragment>
            <Route
                {...rest}
                element={isAuthenticated ? (
                    <Element />
                ) : (
                    <Navigate to="/login" />
                )}
            />
        </React.Fragment>
    );
};

export default PrivateRoute;
