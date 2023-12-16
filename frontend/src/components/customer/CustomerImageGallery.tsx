import { Customer } from "../../../types";
import Card from "./CustomerCard";

interface Props {
  customers: Customer[];
  fetchCustomers: () => void;
  setUpdateCustomer: React.Dispatch<
    React.SetStateAction<Customer | undefined>
  >;
  setOpenCustomerForm: React.Dispatch<React.SetStateAction<boolean>>;
}

const ImageGallery = ({
  customers,
  fetchCustomers,
  setUpdateCustomer,
  setOpenCustomerForm,
}: Props) => {
  return (
    <div className="flex justify-center items-center my-10 gap-3 flex-wrap overflow-y-auto">
      {customers.map((customer) => (
        <Card
          key={customer.id}
          customer={customer}
          fetchCustomers={fetchCustomers}
          setUpdateCustomer={setUpdateCustomer}
          setOpenCustomerForm={setOpenCustomerForm}
        />
      ))}
    </div>
  );
};

export default ImageGallery;
