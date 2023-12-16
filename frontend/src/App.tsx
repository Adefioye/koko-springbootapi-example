import ImageGallery from "./components/ImageGallery";
import { useEffect, useState } from "react";
import { Customer } from "./../types";
import { getCustomers } from "./services/clients";
import SideBarAndNavbar from "./components/shared/SideBarAndNavbar";
import { toast } from "./components/ui/use-toast";
import { AxiosError } from "axios";

function App() {
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [openCustomerForm, setOpenCustomerForm] = useState(false);
  const [updateCustomer, setUpdateCustomer] = useState<
    Customer | undefined
  >(undefined);
  const [error, setError] = useState("");

  async function fetchCustomers() {
    setIsLoading(true);
    console.log()
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
        updateCustomer={updateCustomer}
        setUpdateCustomer={setUpdateCustomer}
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
        updateCustomer={updateCustomer}
        setUpdateCustomer={setUpdateCustomer}
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
        updateCustomer={updateCustomer}
        setUpdateCustomer={setUpdateCustomer}
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
      updateCustomer={updateCustomer}
      setUpdateCustomer={setUpdateCustomer}
    >
      {/* Image gallery */}
      <ImageGallery
        customers={customers}
        fetchCustomers={fetchCustomers}
        setUpdateCustomer={setUpdateCustomer}
        setOpenCustomerForm={setOpenCustomerForm}
      />
    </SideBarAndNavbar>
  );
}

export default App;
