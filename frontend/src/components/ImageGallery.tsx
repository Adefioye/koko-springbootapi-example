import { CustomerProps } from "types";
import Card from "./Card";

interface Props {
  customers: CustomerProps[];
  fetchCustomers: () => void;
}

const ImageGallery = ({ customers, fetchCustomers }: Props) => {
  return (
    <div className="flex justify-center items-center my-10 gap-3 flex-wrap overflow-y-auto">
      {customers.map((customer) => (
        <Card
          key={customer.id}
          customer={customer}
          fetchCustomers={fetchCustomers}
        />
      ))}
    </div>
  );
};

export default ImageGallery;
