import { useState, useEffect } from 'react'
import './App.css'

function App() {
    const [tasks, setTasks] = useState([]);
    const [newTaskTitle, setNewTaskTitle] = useState('');
    const [error, setError] = useState(null);

    // Pagination & Filtering State
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [filter, setFilter] = useState('ALL'); // 'ALL', 'TRUE', 'FALSE'
    const [sortField, setSortField] = useState('id');
    const [sortDir, setSortDir] = useState('asc');

    const API_URL = 'http://localhost:8080/api/v1/tasks';

    // Fetch tasks whenever pagination, sorting, or filtering changes
    useEffect(() => {
        fetchTasks();
    }, [page, filter, sortField, sortDir]);

    const fetchTasks = async () => {
        try {
            let url = `${API_URL}?page=${page}&size=5&sort=${sortField},${sortDir}`;
            if (filter !== 'ALL') {
                url += `&completed=${filter === 'TRUE'}`;
            }

            const response = await fetch(url);
            if (!response.ok) throw new Error('Failed to fetch tasks');

            const data = await response.json();
            setTasks(data.content);
            setTotalPages(data.totalPages);
            setError(null);
        } catch (err) {
            setError(err.message);
        }
    };

    const addTask = async (e) => {
        e.preventDefault();
        if (!newTaskTitle.trim()) return;

        try {
            const response = await fetch(API_URL, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ title: newTaskTitle })
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText);
            }

            setNewTaskTitle('');
            fetchTasks(); // Refresh list after adding
        } catch (err) {
            setError(err.message);
        }
    };

    const markCompleted = async (id) => {
        try {
            const response = await fetch(`${API_URL}/${id}`, { method: 'PUT' });
            if (!response.ok) throw new Error('Failed to update task');
            fetchTasks();
        } catch (err) {
            setError(err.message);
        }
    };

    const deleteTask = async (id) => {
        try {
            const response = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
            if (!response.ok) throw new Error('Failed to delete task');

            // Handle edge case where deleting the last item on a page leaves it empty
            if (tasks.length === 1 && page > 0) {
                setPage(page - 1);
            } else {
                fetchTasks();
            }
        } catch (err) {
            setError(err.message);
        }
    };

    return (
        <div className="container">
            <h1>ToDo Application</h1>

            {error && <div className="error-banner">{error}</div>}

            <form onSubmit={addTask} className="add-task-form">
                <input
                    type="text"
                    placeholder="Enter a new task (min 3 chars)..."
                    value={newTaskTitle}
                    onChange={(e) => setNewTaskTitle(e.target.value)}
                />
                <button type="submit">Add Task</button>
            </form>

            <div className="controls">
                <div className="filter-group">
                    <label>Status Filter: </label>
                    <select value={filter} onChange={(e) => { setFilter(e.target.value); setPage(0); }}>
                        <option value="ALL">All</option>
                        <option value="FALSE">Pending</option>
                        <option value="TRUE">Completed</option>
                    </select>
                </div>

                <div className="sort-group">
                    <label>Sort By: </label>
                    <select value={sortField} onChange={(e) => setSortField(e.target.value)}>
                        <option value="id">Creation Order</option>
                        <option value="title">Alphabetical</option>
                    </select>
                    <button onClick={() => setSortDir(sortDir === 'asc' ? 'desc' : 'asc')}>
                        {sortDir === 'asc' ? '↑' : '↓'}
                    </button>
                </div>
            </div>

            <ul className="task-list">
                {tasks.map(task => (
                    <li key={task.id} className={task.completed ? 'completed' : ''}>
                        <div className="task-content">
                            <input
                                type="checkbox"
                                checked={task.completed}
                                onChange={() => markCompleted(task.id)}
                                disabled={task.completed} // Your PUT API only marks as true
                            />
                            <span>{task.title}</span>
                        </div>
                        <button className="delete-btn" onClick={() => deleteTask(task.id)}>Delete</button>
                    </li>
                ))}
                {tasks.length === 0 && <p className="empty-state">No tasks found.</p>}
            </ul>

            <div className="pagination">
                <button disabled={page === 0} onClick={() => setPage(page - 1)}>Previous</button>
                <span>Page {page + 1} of {Math.max(1, totalPages)}</span>
                <button disabled={page >= totalPages - 1} onClick={() => setPage(page + 1)}>Next</button>
            </div>
        </div>
    )
}

export default App