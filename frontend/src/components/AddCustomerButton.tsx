import { Button } from "./ui/button";

interface Props {
  open?: boolean;
  setOpen: React.Dispatch<React.SetStateAction<boolean>>;
}

const AddCustomerButton = ({ setOpen }: Props) => {
  return (
    <Button
      className="absolute ml-[16rem] top-20"
      onClick={() => setOpen(true)}
    >
      <div className="p-5 flex items-center text-2xl">
        <p>+</p>
        <p>Add Customer</p>
      </div>
    </Button>
  );
};

export default AddCustomerButton;
