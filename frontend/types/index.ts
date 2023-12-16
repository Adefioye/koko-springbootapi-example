export interface Customer {
  id?: number;
  name: string;
  email: string;
  age: number;
  gender: string;
  roles: string[];
  username: string;
}

export interface UserNameAndPassword {
  username: string;
  password: string;
}

export const enum formActionType {
  CREATE_CUSTOMER = "CREATE_CUSTOMER",
  UPDATE_CUSTOMER = "UPDATE_CUSTOMER",
}

export enum genderProps {
  MALE = "MALE",
  FEMALE = "FEMALE",
}

export interface AuthResponse {
  token: string;
  customerDTO: Customer;
}

export interface AuthContextType {
  user: Customer | null;
  login: (credentials: UserNameAndPassword) => Promise<AuthResponse> | Promise<unknown>;
  logout?: () => void;
  isAuthenticated?: boolean;
}
