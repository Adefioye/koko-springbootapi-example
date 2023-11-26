import Navbar from "./components/shared/Navbar";
import LeftSideBar from "./components/shared/LeftSideBar";
import ImageGallery from "./components/ImageGallery";
import { useEffect, useState } from "react";
import { CustomerProps } from "./../types";
import { getCustomers } from "./services/clients";
import CustomerForm from "./components/CustomerForm";
import AddCustomerButton from "./components/AddCustomerButton";

function App() {
  const [customers, setCustomers] = useState<CustomerProps[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [openCustomerForm, setOpenCustomerForm] = useState(false);
  // const [isError, setIsError] = useState(false);


  async function fetchCustomers() {
    setIsLoading(true);
    try {
      const results = await getCustomers();
      setCustomers(results.data);
    } catch (err) {
      console.log(err);
    }
    setIsLoading(false);
  }
  useEffect(() => {
    fetchCustomers();
  }, []);

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (customers.length === 0) {
    return <div>No customers...</div>;
  }

  return (
    <div className="relative flex flex-col min-h-screen text-white bg-slate-600 overflow-y-auto">
      {/* Navbar */}
      <Navbar />
      {/* Left side bar */}
      <LeftSideBar />
      {/* Button to add customer */}
      <AddCustomerButton open={openCustomerForm} setOpen={setOpenCustomerForm} />

      {/* Customer Form */}
      <CustomerForm open={openCustomerForm} setOpen={setOpenCustomerForm} />

      {/* Image gallery */}
      <ImageGallery customers={customers} />
    </div>
  );
}

export default App;
