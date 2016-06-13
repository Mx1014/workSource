//
// EvhCreateContactEntryCommand.m
//
#import "EvhCreateContactEntryCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateContactEntryCommand
//

@implementation EvhCreateContactEntryCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreateContactEntryCommand* obj = [EvhCreateContactEntryCommand new];
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
    if(self.contactId)
        [jsonObject setObject: self.contactId forKey: @"contactId"];
    if(self.entryType)
        [jsonObject setObject: self.entryType forKey: @"entryType"];
    if(self.entryValue)
        [jsonObject setObject: self.entryValue forKey: @"entryValue"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.contactId = [jsonObject objectForKey: @"contactId"];
        if(self.contactId && [self.contactId isEqual:[NSNull null]])
            self.contactId = nil;

        self.entryType = [jsonObject objectForKey: @"entryType"];
        if(self.entryType && [self.entryType isEqual:[NSNull null]])
            self.entryType = nil;

        self.entryValue = [jsonObject objectForKey: @"entryValue"];
        if(self.entryValue && [self.entryValue isEqual:[NSNull null]])
            self.entryValue = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
