import { CustomerProps } from "types";
import Card from "./Card";

interface Props {
  customers: CustomerProps[];
}

const ImageGallery = ({ customers }: Props) => {
  return (
      <div className="flex justify-center items-center my-10 gap-3 flex-wrap overflow-y-auto">
        {customers
          .map((customer) => (
            <Card key={customer.id} customer={customer} />
          ))}
      </div>
  );
};

export default ImageGallery;
