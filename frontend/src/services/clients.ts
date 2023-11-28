import axios from "axios";
import { CustomerProps } from "types";

export async function getCustomers() {
  try {
    return await axios.get(
      `${import.meta.env.VITE_API_BASE_URL}/api/v1/customers`
    );
  } catch (error) {
    console.log(error);
    throw error;
  }
}

export async function saveCustomer(data: CustomerProps) {
  try {
    return await axios.post(
      `${import.meta.env.VITE_API_BASE_URL}/api/v1/customers`,
      data,
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
  } catch (error) {
    console.log(error);
    throw error;
  }
}

export async function deleteCustomer(customerId: number) {
  try {
    return await axios.delete(
      `${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/${customerId}`
    );
  } catch (error) {
    console.log(error);
    throw error;
  }
}
