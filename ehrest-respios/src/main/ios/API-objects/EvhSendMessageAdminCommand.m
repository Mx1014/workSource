//
// EvhSendMessageAdminCommand.m
//
#import "EvhSendMessageAdminCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSendMessageAdminCommand
//

@implementation EvhSendMessageAdminCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSendMessageAdminCommand* obj = [EvhSendMessageAdminCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _meta = [NSMutableDictionary new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.meta) {
        NSMutableDictionary* jsonMap = [NSMutableDictionary new];
        for(NSString* key in self.meta) {
            [jsonMap setValue:[self.meta objectForKey: key] forKey: key];
        }
        [jsonObject setObject: jsonMap forKey: @"meta"];
    }        
    if(self.bodyType)
        [jsonObject setObject: self.bodyType forKey: @"bodyType"];
    if(self.body)
        [jsonObject setObject: self.body forKey: @"body"];
    if(self.deliveryOption)
        [jsonObject setObject: self.deliveryOption forKey: @"deliveryOption"];
    if(self.targetType)
        [jsonObject setObject: self.targetType forKey: @"targetType"];
    if(self.targetToken)
        [jsonObject setObject: self.targetToken forKey: @"targetToken"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSDictionary* jsonMap = [jsonObject objectForKey: @"meta"];
            for(NSString* key in jsonMap) {
                [self.meta setObject: [jsonMap objectForKey: key] forKey: key];
            }
        }
        self.bodyType = [jsonObject objectForKey: @"bodyType"];
        if(self.bodyType && [self.bodyType isEqual:[NSNull null]])
            self.bodyType = nil;

        self.body = [jsonObject objectForKey: @"body"];
        if(self.body && [self.body isEqual:[NSNull null]])
            self.body = nil;

        self.deliveryOption = [jsonObject objectForKey: @"deliveryOption"];
        if(self.deliveryOption && [self.deliveryOption isEqual:[NSNull null]])
            self.deliveryOption = nil;

        self.targetType = [jsonObject objectForKey: @"targetType"];
        if(self.targetType && [self.targetType isEqual:[NSNull null]])
            self.targetType = nil;

        self.targetToken = [jsonObject objectForKey: @"targetToken"];
        if(self.targetToken && [self.targetToken isEqual:[NSNull null]])
            self.targetToken = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
