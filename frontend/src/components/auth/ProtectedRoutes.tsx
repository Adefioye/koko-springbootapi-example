import { useAuth } from "@/context/AuthContext";
import { PropsWithChildren, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "../ui/use-toast";

const ProtectedRoutes = ({ children }: PropsWithChildren) => {
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (!isAuthenticated) {
      setTimeout(() => {
        toast({
          variant: "destructive",
          title: "Oops sorry!",
          description: "You are not logged in",
        });
      }, 100);

      navigate("/");
    }
  }, [isAuthenticated, navigate]);

  return isAuthenticated && children;
};

export default ProtectedRoutes;
