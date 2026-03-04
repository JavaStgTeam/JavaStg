package stg.entity.base;

/**
 * 控制管理器 - 用于管理实体的移动、旋转和加速
 * @since 26-03-04 初始创建
 * @author JavaSTG Team
 */
public class ControlManager {
    private Obj entity; // 被控制的实体
    
    /**
     * 构造函数
     * @param entity 被控制的实体
     */
    public ControlManager(Obj entity) {
        this.entity = entity;
    }
    
    /**
     * 获取被控制的实体
     * @return 被控制的实体
     */
    public Obj getEntity() {
        return entity;
    }
    
    /**
     * 设置被控制的实体
     * @param entity 被控制的实体
     */
    public void setEntity(Obj entity) {
        this.entity = entity;
    }
    
    /**
     * 移动到指定坐标
     * @param x 目标X坐标
     * @param y 目标Y坐标
     * @param time 移动时间（秒）
     */
    public void moveTo(float x, float y, float time) {
        if (entity == null) {
            return;
        }
        
        // 处理时间为0的边界情况
        if (time <= 0) {
            entity.setX(x);
            entity.setY(y);
            entity.setVx(0);
            entity.setVy(0);
            return;
        }
        
        // 计算位移
        float dx = x - entity.getX();
        float dy = y - entity.getY();
        
        // 计算所需速度
        float vx = dx / time;
        float vy = dy / time;
        
        // 设置速度
        entity.setVx(vx);
        entity.setVy(vy);
    }
    
    /**
     * 移动到另一个实体的位置
     * @param targetEntity 目标实体
     * @param time 移动时间（秒）
     */
    public void moveTo(Obj targetEntity, float time) {
        if (entity == null || targetEntity == null) {
            return;
        }
        
        // 获取目标实体的位置
        float targetX = targetEntity.getX();
        float targetY = targetEntity.getY();
        
        // 调用坐标版本的moveTo方法
        moveTo(targetX, targetY, time);
    }
    
    /**
     * 移动指定的距离
     * @param dx X方向位移
     * @param dy Y方向位移
     * @param time 移动时间（秒）
     */
    public void moveBy(float dx, float dy, float time) {
        if (entity == null) {
            return;
        }
        
        // 计算目标位置
        float targetX = entity.getX() + dx;
        float targetY = entity.getY() + dy;
        
        // 调用moveTo方法
        moveTo(targetX, targetY, time);
    }
    
    /**
     * 按指定角度和距离移动
     * @param angle 角度（度），x轴正方向为0，顺时针为正
     * @param range 距离
     * @param time 移动时间（秒）
     */
    public void moveByAngle(float angle, float range, float time) {
        if (entity == null) {
            return;
        }
        
        // 将角度转换为弧度
        double radians = Math.toRadians(angle);
        
        // 计算位移（注意：顺时针为正，所以sin的符号需要调整）
        float dx = (float) (Math.cos(radians) * range);
        float dy = (float) (-Math.sin(radians) * range);
        
        // 调用位移版本的moveBy方法
        moveBy(dx, dy, time);
    }
    
    /**
     * 旋转到指定角度
     * @param angle 目标角度（度），x轴正方向为0，顺时针为正
     * @param time 旋转时间（秒）
     */
    public void rotateTo(float angle, float time) {
        if (entity == null) {
            return;
        }
        
        // 处理时间为0的边界情况
        if (time <= 0) {
            entity.setAngle(angle);
            entity.setAngularVelocity(0);
            return;
        }
        
        // 计算角度差
        float currentAngle = entity.getAngle();
        float angleDiff = angle - currentAngle;
        
        // 计算所需角速度
        float angularVelocity = angleDiff / time;
        
        // 设置角速度
        entity.setAngularVelocity(angularVelocity);
    }
    
    /**
     * 旋转指定的角度
     * @param angle 旋转角度（度），顺时针为正
     * @param time 旋转时间（秒）
     */
    public void rotate(float angle, float time) {
        if (entity == null) {
            return;
        }
        
        // 计算目标角度
        float currentAngle = entity.getAngle();
        float targetAngle = currentAngle + angle;
        
        // 调用rotateTo方法
        rotateTo(targetAngle, time);
    }
    
    /**
     * 以指定加速度加速
     * @param acceleration 加速度
     * @param time 加速时间（秒）
     */
    public void accelerate(float acceleration, float time) {
        if (entity == null) {
            return;
        }
        
        // 处理时间为0的边界情况
        if (time <= 0) {
            return;
        }
        
        // 计算当前速度的大小和方向
        float currentVx = entity.getVx();
        float currentVy = entity.getVy();
        float currentSpeed = (float) Math.sqrt(currentVx * currentVx + currentVy * currentVy);
        
        // 计算目标速度
        float targetSpeed = currentSpeed + acceleration * time;
        
        // 如果当前速度为0，保持原方向（默认为x轴正方向）
        if (currentSpeed == 0) {
            float vx = targetSpeed;
            float vy = 0;
            entity.setVx(vx);
            entity.setVy(vy);
            return;
        }
        
        // 计算速度方向的单位向量
        float unitVx = currentVx / currentSpeed;
        float unitVy = currentVy / currentSpeed;
        
        // 计算目标速度的分量
        float targetVx = unitVx * targetSpeed;
        float targetVy = unitVy * targetSpeed;
        
        // 计算速度变化率
        float deltaVx = (targetVx - currentVx) / time;
        float deltaVy = (targetVy - currentVy) / time;
        
        // 设置新的速度（这里直接设置目标速度，实际应用中可能需要在update方法中逐渐调整速度）
        entity.setVx(targetVx);
        entity.setVy(targetVy);
    }
    
    /**
     * 以轨道运动
     * @param orbit 轨道
     * @param speed 速度（度/秒）
     */
    public void moveAt(Orbit orbit, float speed) {
        if (entity == null || orbit == null) {
            return;
        }
        
        // 设置角速度
        entity.setAngularVelocity(speed);
        
        // 计算当前角度对应的轨道位置
        float currentAngle = entity.getAngle();
        float[] position = orbit.getPointAtAngle(currentAngle);
        
        // 设置实体位置
        entity.setX(position[0]);
        entity.setY(position[1]);
    }
}