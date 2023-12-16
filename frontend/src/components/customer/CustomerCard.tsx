import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Customer } from "../../../types";
import { Button } from "../ui/button";
import DeleteCustomerDialog from "./DeleteCustomerDialog";
import { useState } from "react";

interface Props {
  customer: Customer;
  fetchCustomers: () => void;
  setUpdateCustomer: React.Dispatch<
    React.SetStateAction<Customer | undefined>
  >;
  setOpenCustomerForm: React.Dispatch<React.SetStateAction<boolean>>;
}

const Card = ({
  customer,
  fetchCustomers,
  setUpdateCustomer,
  setOpenCustomerForm,
}: Props) => {
  const [openDialog, setOpenDialog] = useState(false);

  const randomUserGenerator = customer.gender === "MALE" ? "men" : "women";

  function handleCustomerUpdate(customer: Customer): void {
    // Set local customer state and populate form with that information
    setUpdateCustomer(customer);
    setOpenCustomerForm(true);
  }

  return (
    <div className="flex flex-col max-w-sm items-center border border-white rounded-xl">
      <img
        src="https://images.unsplash.com/photo-1612865547334-09cb8cb455da?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=634&q=80"
        alt="card pics"
        className="h-[5rem] w-[15rem] object-cover rounded-xl"
      />
      <Avatar className="-mt-4 border-4 border-white">
        <AvatarImage
          src={`https://randomuser.me/api/portraits/thumb/${randomUserGenerator}/${customer.id}.jpg`}
        />
        <AvatarFallback>CN</AvatarFallback>
      </Avatar>
      <div className="flex p-2 flex-col mt-2 space-y-3 items-center break-all">
        <p>{customer.id}</p>
        <p>{customer.name}</p>
        <p>{customer.email}</p>
        <p>
          {customer.age} | {customer.gender}
        </p>
        <div className="flex space-x-8">
          <Button onClick={() => handleCustomerUpdate(customer)}>Update</Button>
          <Button variant="destructive" onClick={() => setOpenDialog(true)}>
            Delete
          </Button>
        </div>
        {/* Delete customer dialog */}
        <DeleteCustomerDialog
          openDialog={openDialog}
          setOpenDialog={setOpenDialog}
          customer={customer}
          fetchCustomers={fetchCustomers}
        />
      </div>
    </div>
  );
};

export default Card;
