import axios from "axios";

export async function getCustomers() {
  try {
    return await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers`);
  } catch (error) {
    console.log(error);
    throw error;
  }
}
