//
// EvhPmsyAddressDTO.m
//
#import "EvhPmsyAddressDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmsyAddressDTO
//

@implementation EvhPmsyAddressDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPmsyAddressDTO* obj = [EvhPmsyAddressDTO new];
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
    if(self.projectId)
        [jsonObject setObject: self.projectId forKey: @"projectId"];
    if(self.resourceId)
        [jsonObject setObject: self.resourceId forKey: @"resourceId"];
    if(self.resourceName)
        [jsonObject setObject: self.resourceName forKey: @"resourceName"];
    if(self.payerId)
        [jsonObject setObject: self.payerId forKey: @"payerId"];
    if(self.customerId)
        [jsonObject setObject: self.customerId forKey: @"customerId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.projectId = [jsonObject objectForKey: @"projectId"];
        if(self.projectId && [self.projectId isEqual:[NSNull null]])
            self.projectId = nil;

        self.resourceId = [jsonObject objectForKey: @"resourceId"];
        if(self.resourceId && [self.resourceId isEqual:[NSNull null]])
            self.resourceId = nil;

        self.resourceName = [jsonObject objectForKey: @"resourceName"];
        if(self.resourceName && [self.resourceName isEqual:[NSNull null]])
            self.resourceName = nil;

        self.payerId = [jsonObject objectForKey: @"payerId"];
        if(self.payerId && [self.payerId isEqual:[NSNull null]])
            self.payerId = nil;

        self.customerId = [jsonObject objectForKey: @"customerId"];
        if(self.customerId && [self.customerId isEqual:[NSNull null]])
            self.customerId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
