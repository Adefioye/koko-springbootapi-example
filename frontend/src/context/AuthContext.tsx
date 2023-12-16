import { PropsWithChildren, createContext, useContext, useState } from "react";
import { AuthContextType, Customer, UserNameAndPassword } from "types";
import { login as performLogin } from "@/services/clients";

export const ACCESS_TOKEN = "access_token";

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: PropsWithChildren) => {
  const [user, setUser] = useState<Customer | null>(null);

  const login = async (credentials: UserNameAndPassword) => {
    return new Promise((resolve, reject) => {
      performLogin(credentials)
        .then((res) => {
          const jwtToken = res.headers["authorization"];
          localStorage.setItem(ACCESS_TOKEN, jwtToken);
          // TODO set User
          setUser({ ...res.data.customerDTO });
          resolve(res);
        })
        .catch((err) => {
          reject(err);
        });
    });
  };

  const value = { user, login };

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
