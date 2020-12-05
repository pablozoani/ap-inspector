import axios from "axios"
import React, { useEffect, useState } from "react"
import { useDispatch, useSelector } from "react-redux"
import { API_BASE_URL } from "../../../store/constants"
import { setScene } from "../../../store/Main/actions/actions"
import { RootState } from "../../../store/rootReducer"
import { HOME } from "../../scenes/scenes"
import classes from "./AddProduct.module.css"

export type AddProductProps = {}

const AddProduct: React.FC<AddProductProps> = (props) => {
    const dispatch = useDispatch()
    const [productName, setProductName] = useState("")
    const [price, setPrice] = useState("0.00")
    const [vendorId, setVendorId] = useState("")
    const [categoryId, setCategoryId] = useState("")
    const vendors = useSelector((state: RootState) => state.home.vendors)
    const categories = useSelector((state: RootState) => state.home.categories)

    const categoryOptions: JSX.Element[] = []

    const loadCategories = () => {
        categoryOptions.push(
            <option value="" key="">
                Select a category...
            </option>
        )
        categories.forEach((c) => {
            categoryOptions.push(
                <option key={c.id} value={c.id}>
                    {c.name}
                </option>
            )
        })
    }
    loadCategories()

    const vendorOptions: JSX.Element[] = []

    const loadVendors = () => {
        vendorOptions.push(
            <option key="" id="">
                Select a vendor...
            </option>
        )
        vendors.forEach((v) => {
            vendorOptions.push(
                <option key={v.id} value={v.id}>
                    {`${v.firstName} ${v.lastName}`}
                </option>
            )
        })
    }
    loadVendors()

    return (
        <div className={classes.AddProduct}>
            <div
                style={{
                    display: "flex",
                    flexDirection: "column",
                    gap: "1rem"
                }}
            >
                <input
                    type="text"
                    className={classes.TextField}
                    placeholder="Product name..."
                    value={productName}
                    onChange={(event) =>
                        setProductName(event.currentTarget.value)
                    }
                />
                <input
                    className={classes.TextField}
                    type="text"
                    value={"$ " + price}
                    onChange={(event) => {
                        const str = event.currentTarget.value.substring(2)
                        if (/^[0-9]*\.?[0-9]{0,2}$/.test(str)) {
                            setPrice(str)
                        }
                    }}
                />
                <select
                    className={classes.Select}
                    onChange={(event) => setVendorId(event.currentTarget.value)}
                >
                    {vendorOptions}
                </select>
                <select
                    className={classes.Select}
                    onChange={(event) =>
                        setCategoryId(event.currentTarget.value)
                    }
                >
                    {categoryOptions}
                </select>
                <button
                    type="button"
                    onClick={() => {
                        const data = {
                            name: productName,
                            price: +price,
                            vendorId,
                            categoryId
                        }
                        axios
                            .post(API_BASE_URL + "/api/v1/products", data)
                            .catch((err) => console.log(err))
                            .finally(() => dispatch(setScene(HOME)))
                    }}
                >
                    Add
                </button>
                <button type="button" onClick={() => dispatch(setScene(HOME))}>
                    Cancel
                </button>
            </div>
        </div>
    )
}

export default AddProduct
