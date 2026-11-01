// one measurement; the colour tells if it is in range (flag = WARNING or CRITICAL when it is not)
export default function Vital({ label, value, unit, flag }) {
  return (
    <div className={'vital' + (flag ? ' ' + flag.toLowerCase() : '')}>
      <span className="vital-label">{label}</span>
      <span className="vital-value">
        {value}
        <small> {unit}</small>
      </span>
    </div>
  );
}
