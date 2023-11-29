import { CustomerProps } from "types";
import { Button } from "./ui/button";

interface Props {
  open?: boolean;
  setOpen: React.Dispatch<React.SetStateAction<boolean>>;
  setUpdateCustomer: React.Dispatch<
    React.SetStateAction<CustomerProps | undefined>
  >;
}

const AddCustomerButton = ({ setOpen, setUpdateCustomer }: Props) => {
  return (
    <Button
      className="absolute ml-[16rem] top-20"
      onClick={() => {
        setUpdateCustomer(undefined);
        setOpen(true);
      }}
    >
      <div className="p-5 flex items-center text-2xl">
        <p>+</p>
        <p>Customer</p>
      </div>
    </Button>
  );
};

export default AddCustomerButton;
