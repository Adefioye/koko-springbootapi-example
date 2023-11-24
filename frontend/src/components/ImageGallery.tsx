import { CustomerProps } from "types";
import Card from "./Card";

interface Props {
  customers: CustomerProps[];
}

const ImageGallery = ({ customers }: Props) => {
  return (
    <div className="absolute top-24 left-[15rem] w[100vw - 15rem] mx-auto">
      <div className="flex justify-center gap-3 flex-wrap m-10 overflow-y-auto">
        {customers
          .map((customer) => (
            <Card key={customer.id} customer={customer} />
          ))}
      </div>
    </div>
  );
};

export default ImageGallery;
