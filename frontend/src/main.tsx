import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App.tsx";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import "./index.css";
import { Toaster } from "./components/ui/toaster.tsx";
import LoginForm from "./components/auth/Login.tsx";
import { AuthProvider } from "./context/AuthContext.tsx";
import SignupForm from "./components/auth/SignupForm.tsx";
import ProtectedRoutes from "./components/auth/ProtectedRoutes.tsx";

const router = createBrowserRouter([
  {
    path: "/",
    element: <LoginForm />,
  },
  {
    path: "/signup",
    element: <SignupForm />,
  },
  {
    path: "/dashboard",

    element: (
      <ProtectedRoutes>
        <App />
      </ProtectedRoutes>
    ),
  },
]);

ReactDOM.createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <AuthProvider>
      <RouterProvider router={router} />
      <Toaster />
    </AuthProvider>
  </React.StrictMode>
);
