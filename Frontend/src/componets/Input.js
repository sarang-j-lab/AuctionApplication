import React from 'react'

const Input = ({field ,index}) => {
    return (
        <div key={index}>
            <label
                htmlFor={field.name}
                className="block text-sm font-medium text-gray-700"
            >
                {field.label}
            </label>
            <input
                type={field.type}
                id={field.name}
                name={field.name}
                value={formData[field.name]}
                onChange={handleChange}
                className="mt-1 p-3 block w-full rounded-md border-2 border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 sm:text-sm"
                required
            />
        </div>
    )
}

export default Input