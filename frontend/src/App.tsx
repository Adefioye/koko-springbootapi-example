import ImageGallery from "./components/ImageGallery";
import { useEffect, useState } from "react";
import { CustomerProps } from "./../types";
import { getCustomers } from "./services/clients";
import SideBarAndNavbar from "./components/shared/SideBarAndNavbar";

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
    return (
      <SideBarAndNavbar open={openCustomerForm} setOpen={setOpenCustomerForm} fetchCustomers={fetchCustomers}>
        <p>Loading...</p>
      </SideBarAndNavbar>
    );
  }

  if (customers.length === 0) {
    return (
      <SideBarAndNavbar open={openCustomerForm} setOpen={setOpenCustomerForm} fetchCustomers={fetchCustomers}>
        <p>No customers</p>
      </SideBarAndNavbar>
    );
  }

  return (
    <SideBarAndNavbar open={openCustomerForm} setOpen={setOpenCustomerForm} fetchCustomers={fetchCustomers}>
      {/* Image gallery */}
      <ImageGallery customers={customers} />
    </SideBarAndNavbar>
  );
}

export default App;
