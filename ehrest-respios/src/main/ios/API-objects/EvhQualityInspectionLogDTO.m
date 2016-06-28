//
// EvhQualityInspectionLogDTO.m
//
#import "EvhQualityInspectionLogDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQualityInspectionLogDTO
//

@implementation EvhQualityInspectionLogDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhQualityInspectionLogDTO* obj = [EvhQualityInspectionLogDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.operatorId)
        [jsonObject setObject: self.operatorId forKey: @"operatorId"];
    if(self.operatorName)
        [jsonObject setObject: self.operatorName forKey: @"operatorName"];
    if(self.operateType)
        [jsonObject setObject: self.operateType forKey: @"operateType"];
    if(self.targetType)
        [jsonObject setObject: self.targetType forKey: @"targetType"];
    if(self.targetId)
        [jsonObject setObject: self.targetId forKey: @"targetId"];
    if(self.targetName)
        [jsonObject setObject: self.targetName forKey: @"targetName"];
    if(self.operateTime)
        [jsonObject setObject: self.operateTime forKey: @"operateTime"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.operatorId = [jsonObject objectForKey: @"operatorId"];
        if(self.operatorId && [self.operatorId isEqual:[NSNull null]])
            self.operatorId = nil;

        self.operatorName = [jsonObject objectForKey: @"operatorName"];
        if(self.operatorName && [self.operatorName isEqual:[NSNull null]])
            self.operatorName = nil;

        self.operateType = [jsonObject objectForKey: @"operateType"];
        if(self.operateType && [self.operateType isEqual:[NSNull null]])
            self.operateType = nil;

        self.targetType = [jsonObject objectForKey: @"targetType"];
        if(self.targetType && [self.targetType isEqual:[NSNull null]])
            self.targetType = nil;

        self.targetId = [jsonObject objectForKey: @"targetId"];
        if(self.targetId && [self.targetId isEqual:[NSNull null]])
            self.targetId = nil;

        self.targetName = [jsonObject objectForKey: @"targetName"];
        if(self.targetName && [self.targetName isEqual:[NSNull null]])
            self.targetName = nil;

        self.operateTime = [jsonObject objectForKey: @"operateTime"];
        if(self.operateTime && [self.operateTime isEqual:[NSNull null]])
            self.operateTime = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
