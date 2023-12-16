import {
  PropsWithChildren,
  createContext,
  useContext,
  useEffect,
  useState,
} from "react";
import { AuthContextType, UserNameAndPassword } from "types";
import { login as performLogin } from "@/services/clients";
import { jwtDecode } from "jwt-decode";

export const ACCESS_TOKEN = "access_token";

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: PropsWithChildren) => {
  const [user, setUser] = useState<string | null>(null);

  const setUserFromToken = () => {
    const token = localStorage.getItem("access_token");
    if (token) {
      const decoded = jwtDecode(token);
      console.log(decoded);
      setUser(decoded.sub ?? "");
    }
  };
  useEffect(() => {
    setUserFromToken();
  }, []);

  const login = async (credentials: UserNameAndPassword) => {
    return new Promise((resolve, reject) => {
      performLogin(credentials)
        .then((res) => {
          const jwtToken = res.headers["authorization"];
          localStorage.setItem(ACCESS_TOKEN, jwtToken);
          setUser({ ...res.data.customerDTO });
          resolve(res);
        })
        .catch((err) => {
          reject(err);
        });
    });
  };

  const logout = () => {
    localStorage.removeItem(ACCESS_TOKEN);
    setUser(null);
  };

  const isAuthenticated = localStorage.getItem(ACCESS_TOKEN) ? true : false;

  const value = { user, login, logout, isAuthenticated, setUserFromToken };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

// eslint-disable-next-line react-refresh/only-export-components
export const useAuth = () => {
  const context = useContext(AuthContext);

  if (!context) {
    throw new Error("useAuth must be used inside AuthProvider");
  }

  return context;
};
