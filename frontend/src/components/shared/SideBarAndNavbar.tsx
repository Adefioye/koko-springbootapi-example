import Navbar from "./Navbar";
import LeftSideBar from "./LeftSideBar";
import AddCustomerButton from "../AddCustomerButton";
import { ReactNode } from "react";
import CustomerForm from "../CustomerForm";

interface Props {
  open: boolean;
  setOpen: React.Dispatch<React.SetStateAction<boolean>>;
  children: ReactNode;
  fetchCustomers: () => void;
}

const SideBarAndNavbar = ({ open, setOpen, children, fetchCustomers }: Props) => {
  return (
    <div className="relative flex flex-col min-h-screen text-white bg-slate-600 overflow-y-auto">
      <Navbar />
      <LeftSideBar />
      {/* Button to add customer */}
      <AddCustomerButton open={open} setOpen={setOpen} />
      {/* Customer Form */}
      <CustomerForm open={open} setOpen={setOpen} fetchCustomers={fetchCustomers} />
      <div className="absolute top-24 left-[15rem] mt-10 ml-10 w[100vw - 15rem]">
        {children}
      </div>
    </div>
  );
};

export default SideBarAndNavbar;
