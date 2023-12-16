import { useAuth } from "@/context/AuthContext";
import { Button } from "../ui/button";

const Navbar = () => {
  const { isAuthenticated, logout } = useAuth();

  function handleLogout(): void {
    logout();
  }

  return (
    <div className="p-5 shadow-md sticky flex justify-end ml-[15rem]">
      <Button onClick={handleLogout}>
        {isAuthenticated ? "Sign out" : "Sign in"}
      </Button>
    </div>
  );
};

export default Navbar;
