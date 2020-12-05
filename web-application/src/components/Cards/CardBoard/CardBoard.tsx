import React from "react"
import Backdrop from "../../Backdrop/Backdrop"
import Card from "../Card/Card"
import classes from "./CardBoard.module.css"

type ContainerProps = {}

const Container: React.FC<ContainerProps> = (props) => {
    return <div className={classes.Container}>{props.children}</div>
}

export type CardBoardProps = {
    cards: JSX.Element[]
    pageLeft: () => void
    pageRight: () => void
    pageFirst: () => void
    pageLast: () => void
    currentPage: number
}

const CardBoard: React.FC<CardBoardProps> = (props) => {
    const output = (
        <div
            style={{
                display: "flex",
                flexFlow: "column",
                gap: "1rem",
                width: "auto"
            }}
        >
            <div className={classes.CardBoard}>{props.cards}</div>
            <div
                style={{
                    display: "flex",
                    justifyContent: "space-between"
                }}
            >
                <span style={{ display: "flex", gap: "1rem" }}>
                    <button
                        className={classes.PageButton}
                        onClick={props.pageFirst}
                        title="first page"
                    >
                        {"l<"}
                    </button>
                    <button
                        className={classes.PageButton}
                        onClick={props.pageLeft}
                        title="previous page"
                    >
                        {"<"}
                    </button>
                </span>
                <span style={{ color: "#eee" }}>{props.currentPage}</span>
                <span style={{ display: "flex", gap: "1rem" }}>
                    <button
                        className={classes.PageButton}
                        onClick={props.pageRight}
                        title="next page"
                    >
                        {">"}
                    </button>
                    <button
                        className={classes.PageButton}
                        onClick={props.pageLast}
                        title="last page"
                    >
                        {">l"}
                    </button>
                </span>
            </div>
        </div>
    )
    return output
}

export default CardBoard
