import Navbar from "./Navbar";
import LeftSideBar from "./LeftSideBar";
import AddCustomerButton from "../customer/AddCustomerButton";
import { ReactNode } from "react";
import CustomerForm from "../customer/CustomerForm";
import { Customer } from "../../../types";

interface Props {
  open: boolean;
  setOpen: React.Dispatch<React.SetStateAction<boolean>>;
  children: ReactNode;
  fetchCustomers: () => void;
  updateCustomer: Customer | undefined;
  setUpdateCustomer: React.Dispatch<React.SetStateAction<Customer | undefined>>;
}

const SideBarAndNavbar = ({
  open,
  setOpen,
  children,
  fetchCustomers,
  updateCustomer,
  setUpdateCustomer,
}: Props) => {
  return (
    <div className="relative flex flex-col min-h-screen text-white bg-slate-600 overflow-y-auto">
      <Navbar />
      <LeftSideBar />
      {/* Button to add customer */}
      <AddCustomerButton
        open={open}
        setOpen={setOpen}
        setUpdateCustomer={setUpdateCustomer}
      />
      {/* Customer Form */}
      <CustomerForm
        open={open}
        setOpen={setOpen}
        fetchCustomers={fetchCustomers}
        updateCustomer={updateCustomer}
      />
      <div className="absolute top-24 left-[15rem] mt-10 ml-10 w[100vw - 15rem]">
        {children}
      </div>
    </div>
  );
};

export default SideBarAndNavbar;
