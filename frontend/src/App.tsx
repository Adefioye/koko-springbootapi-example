import ImageGallery from "./components/ImageGallery";
import { useEffect, useState } from "react";
import { CustomerProps } from "./../types";
import { getCustomers } from "./services/clients";
import SideBarAndNavbar from "./components/shared/SideBarAndNavbar";
import { toast } from "./components/ui/use-toast";
import { AxiosError } from "axios";

function App() {
  const [customers, setCustomers] = useState<CustomerProps[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [openCustomerForm, setOpenCustomerForm] = useState(false);
  const [error, setError] = useState("");

  async function fetchCustomers() {
    setIsLoading(true);
    try {
      const results = await getCustomers();
      setCustomers(results.data);
    } catch (error) {
      if (error instanceof AxiosError) {
        setError(error.response?.data.message);
        toast({
          variant: "destructive",
          title: `${error.code}`,
          description: `${error.response?.data?.message}`,
        });
      }
    }
    setIsLoading(false);
  }
  useEffect(() => {
    fetchCustomers();
  }, []);

  if (isLoading) {
    return (
      <SideBarAndNavbar
        open={openCustomerForm}
        setOpen={setOpenCustomerForm}
        fetchCustomers={fetchCustomers}
      >
        <p>Loading...</p>
      </SideBarAndNavbar>
    );
  }

  if (error) {
    return (
      <SideBarAndNavbar
        open={openCustomerForm}
        setOpen={setOpenCustomerForm}
        fetchCustomers={fetchCustomers}
      >
        <p>Ooops error occured...</p>
      </SideBarAndNavbar>
    );
  }

  if (customers.length === 0) {
    return (
      <SideBarAndNavbar
        open={openCustomerForm}
        setOpen={setOpenCustomerForm}
        fetchCustomers={fetchCustomers}
      >
        <p>No customers</p>
      </SideBarAndNavbar>
    );
  }

  return (
    <SideBarAndNavbar
      open={openCustomerForm}
      setOpen={setOpenCustomerForm}
      fetchCustomers={fetchCustomers}
    >
      {/* Image gallery */}
      <ImageGallery customers={customers} fetchCustomers={fetchCustomers} />
    </SideBarAndNavbar>
  );
}

export default App;
