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
    console.log("Isauthenticated: ", isAuthenticated)
    if (!isAuthenticated) navigate("/");
  }, [isAuthenticated, navigate]);

  return (
    <div className="w-full p-5 shadow-md sticky ml-[15rem]">
      <Button onClick={handleLogout} className="">
        {isAuthenticated ? "Sign out" : "Sign in"}
      </Button>
    </div>
  );
};

export default Navbar;
