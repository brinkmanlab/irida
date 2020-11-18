import React from "react";
import { IconShoppingCart } from "../../../icons/Icons";
import { Badge } from "antd";
import { CART } from "../../../../utilities/events-utilities";
import { setBaseUrl } from "../../../../utilities/url-utilities";
import { getCartCount } from "../../../../apis/cart/cart";

/**
 * React component to display the cart icon and current counts in the
 * IU header.
 * @returns {JSX.Element}
 * @constructor
 */
export function CartLink() {
  const [count, setCount] = React.useState(0);
  /*
  If we are inside a galaxy session then the cart should direct to the galaxy.
  TODO: Move this logic to the cart not the main navigation
   */
  const inGalaxy = typeof window.GALAXY !== "undefined";

  function updateCount(e) {
    const { count: newCount } = e.detail;
    setCount(newCount);
  }

  // Initialize cart here
  React.useEffect(() => {
    getCartCount().then(({ count: newCount }) => setCount(newCount));
  }, []);

  React.useEffect(() => {
    document.addEventListener(CART.UPDATED, updateCount, false);
    return () => document.removeEventListener(CART.UPDATED, updateCount, false);
  }, []);

  return (
    <Badge count={count}>
      <a href={setBaseUrl(`/cart/${inGalaxy ? "galaxy" : "pipelines"}`)}>
        <IconShoppingCart />
      </a>
    </Badge>
  );
}
