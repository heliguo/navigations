export class DataSource {

    _data: any;
    _keyExtractor: any;
    private _listeners: any;

    constructor(data: any, keyExtractor: any) {
        this._data = data || [];
        this._keyExtractor = keyExtractor;
        this._listeners = [];
        if (!keyExtractor) {
            console.warn(
                'RecyclerViewList/DataSource: missing keyExtractor, it\'s strongly recommended to specify a keyExtractor function ' +
                'in order to use all the features correctly.',
            );
            this._keyExtractor = (item: any, index: any) => {
                return JSON.stringify(item) + '_' + index;
            };
        }
    }

    push(item: DataSource) {
        this._data.push(item);
        this._listeners.forEach((listener: any) => {
            listener && listener.onPush && listener.onPush(item);
        });
    }

    unshift(item: DataSource) {
        this._data.unshift(item);
        this._listeners.forEach((listener: any) => {
            listener && listener.onUnshift && listener.onUnshift(item);
        });
    }

    splice(start: number, deleteCount: number, items: DataSource[]) {
        this._data.splice(start, deleteCount, items);
        this._listeners.forEach((listener: any) => {
            listener && listener.onSplice && listener.onSplice(start, deleteCount, items);
        });
    }

    size(): number {
        return this._data.length;
    }

    moveUp(index: number) {
        if (index <= 0) {
            return;
        }
        const item = this._data[index];
        this._data[index] = this._data[index - 1];
        this._data[index - 1] = item;
        this._listeners.forEach((listener: any) => {
            listener && listener.onMoveUp && listener.onMoveUp(index);
        });
    }

    moveDown(index: number) {
        if (index >= this._data.length - 1) {
            return;
        }
        const item = this._data[index];
        this._data[index] = this._data[index + 1];
        this._data[index + 1] = item;
        this._listeners.forEach((listener: any) => {
            listener && listener.onMoveDown && listener.onMoveDown(index);
        });
    }

    set(index: number, item: DataSource) {
        this._data[index] = item;
        this._listeners.forEach((listener: any) => {
            listener && listener.onSplice && listener.onSet(index, item);
        });
    }

    setDirty() {
        this._listeners.forEach((listener: any) => {
            listener && listener.onSetDirty && listener.onSetDirty();
        });
    }

    get(index: number) {
        return this._data[index];
    }

    getKey(item: DataSource, index: number) {
        return this._keyExtractor(item, index);
    }

    position(item: DataSource) {
        return this._data.indexOf(item);
    }

    _addListener(listener: any) {
        this._listeners.push(listener);
    }

    _removeListener(listener: any) {
        let index = this._listeners.indexOf(listener);
        if (index > -1) {
            this._listeners.splice(index, 1);
        }
    }

}
