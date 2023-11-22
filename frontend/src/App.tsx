import Navbar from "./components/shared/Navbar";
import LeftSideBar from "./components/shared/LeftSideBar";
import ImageGallery from "./components/ImageGallery";
import { useEffect, useState } from "react";
import { CustomerProps } from "./../types";
import { getCustomers } from "./services/clients";

function App() {
  const [customers, setCustomers] = useState<CustomerProps[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  // const [isError, setIsError] = useState(false);

  async function fetchCustomers() {
    setIsLoading(true);
    try {
      const results = await getCustomers();
      console.log(results);
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
    return <div>No customers...</div>
  }

  return (
    <div className="relative flex flex-col min-h-screen text-white bg-slate-600 overflow-y-auto">
      {/* Navbar */}
      <Navbar />
      {/* Left side bar */}
      <LeftSideBar />
      {/* Image gallery */}
      <ImageGallery customers={customers} />
    </div>
  );
}

export default App;
