import { useAuth } from "@/context/AuthContext";
import { Button } from "../ui/button";
import { useNavigate } from "react-router-dom";
import { useEffect } from "react";

const Navbar = () => {
  const { isAuthenticated, logout } = useAuth();
  const navigate = useNavigate();

  function handleLogout(): void {
    logout();
  }

  useEffect(() => {
    if (!isAuthenticated) navigate("/");
  }, [isAuthenticated, navigate]);

  return (
    <div className="p-5 shadow-md sticky flex justify-end ml-[15rem]">
      <Button onClick={handleLogout}>
        {isAuthenticated ? "Sign out" : "Sign in"}
      </Button>
    </div>
  );
};

export default Navbar;
