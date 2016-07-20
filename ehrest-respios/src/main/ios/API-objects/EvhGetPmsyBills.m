//
// EvhGetPmsyBills.m
//
#import "EvhGetPmsyBills.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetPmsyBills
//

@implementation EvhGetPmsyBills

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetPmsyBills* obj = [EvhGetPmsyBills new];
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
    if(self.customerId)
        [jsonObject setObject: self.customerId forKey: @"customerId"];
    if(self.projectId)
        [jsonObject setObject: self.projectId forKey: @"projectId"];
    if(self.resourceId)
        [jsonObject setObject: self.resourceId forKey: @"resourceId"];
    if(self.billDateStr)
        [jsonObject setObject: self.billDateStr forKey: @"billDateStr"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.customerId = [jsonObject objectForKey: @"customerId"];
        if(self.customerId && [self.customerId isEqual:[NSNull null]])
            self.customerId = nil;

        self.projectId = [jsonObject objectForKey: @"projectId"];
        if(self.projectId && [self.projectId isEqual:[NSNull null]])
            self.projectId = nil;

        self.resourceId = [jsonObject objectForKey: @"resourceId"];
        if(self.resourceId && [self.resourceId isEqual:[NSNull null]])
            self.resourceId = nil;

        self.billDateStr = [jsonObject objectForKey: @"billDateStr"];
        if(self.billDateStr && [self.billDateStr isEqual:[NSNull null]])
            self.billDateStr = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
