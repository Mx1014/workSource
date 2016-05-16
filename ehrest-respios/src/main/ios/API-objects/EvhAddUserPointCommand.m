//
// EvhAddUserPointCommand.m
//
#import "EvhAddUserPointCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddUserPointCommand
//

@implementation EvhAddUserPointCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAddUserPointCommand* obj = [EvhAddUserPointCommand new];
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
    if(self.operatorUid)
        [jsonObject setObject: self.operatorUid forKey: @"operatorUid"];
    if(self.pointType)
        [jsonObject setObject: self.pointType forKey: @"pointType"];
    if(self.point)
        [jsonObject setObject: self.point forKey: @"point"];
    if(self.uid)
        [jsonObject setObject: self.uid forKey: @"uid"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.operatorUid = [jsonObject objectForKey: @"operatorUid"];
        if(self.operatorUid && [self.operatorUid isEqual:[NSNull null]])
            self.operatorUid = nil;

        self.pointType = [jsonObject objectForKey: @"pointType"];
        if(self.pointType && [self.pointType isEqual:[NSNull null]])
            self.pointType = nil;

        self.point = [jsonObject objectForKey: @"point"];
        if(self.point && [self.point isEqual:[NSNull null]])
            self.point = nil;

        self.uid = [jsonObject objectForKey: @"uid"];
        if(self.uid && [self.uid isEqual:[NSNull null]])
            self.uid = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
