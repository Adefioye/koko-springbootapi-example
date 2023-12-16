import axios from "axios";
import { Customer, UserNameAndPassword } from "../../types";
import { ACCESS_TOKEN } from "@/context/AuthContext";

function getAuthConfig() {
  return {
    headers: { Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}` },
  };
}

export async function getCustomers() {
  try {
    return await axios.get(
      `${import.meta.env.VITE_API_BASE_URL}/api/v1/customers`,
      getAuthConfig()
    );
  } catch (error) {
    console.log(error);
    throw error;
  }
}

export async function saveCustomer(data: Customer) {
  try {
    return await axios.post(
      `${import.meta.env.VITE_API_BASE_URL}/api/v1/customers`,
      data,
    );
  } catch (error) {
    console.log(error);
    throw error;
  }
}

export async function updateCustomer(customerId: number, data: Customer) {
  try {
    return await axios.put(
      `${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/${customerId}`,
      data,
      getAuthConfig()
    );
  } catch (error) {
    console.log(error);
    throw error;
  }
}

export async function deleteCustomer(customerId: number) {
  try {
    return await axios.delete(
      `${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/${customerId}`,
      getAuthConfig()
    );
  } catch (error) {
    console.log(error);
    throw error;
  }
}

export async function login(userNameAndPassword: UserNameAndPassword) {
  try {
    return await axios.post(
      `${import.meta.env.VITE_API_BASE_URL}/api/v1/auth/login`,
      userNameAndPassword,
    );
  } catch (error) {
    console.log(error);
    throw error;
  }
}
