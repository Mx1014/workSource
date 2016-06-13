//
// EvhCreateGroupContactCommand.m
//
#import "EvhCreateGroupContactCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateGroupContactCommand
//

@implementation EvhCreateGroupContactCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreateGroupContactCommand* obj = [EvhCreateGroupContactCommand new];
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
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
    if(self.contactName)
        [jsonObject setObject: self.contactName forKey: @"contactName"];
    if(self.contactToken)
        [jsonObject setObject: self.contactToken forKey: @"contactToken"];
    if(self.department)
        [jsonObject setObject: self.department forKey: @"department"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.ownerId = [jsonObject objectForKey: @"ownerId"];
        if(self.ownerId && [self.ownerId isEqual:[NSNull null]])
            self.ownerId = nil;

        self.contactName = [jsonObject objectForKey: @"contactName"];
        if(self.contactName && [self.contactName isEqual:[NSNull null]])
            self.contactName = nil;

        self.contactToken = [jsonObject objectForKey: @"contactToken"];
        if(self.contactToken && [self.contactToken isEqual:[NSNull null]])
            self.contactToken = nil;

        self.department = [jsonObject objectForKey: @"department"];
        if(self.department && [self.department isEqual:[NSNull null]])
            self.department = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
