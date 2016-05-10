//
// EvhEnterpriseContactEntryDTO.m
//
#import "EvhEnterpriseContactEntryDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEnterpriseContactEntryDTO
//

@implementation EvhEnterpriseContactEntryDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhEnterpriseContactEntryDTO* obj = [EvhEnterpriseContactEntryDTO new];
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
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.contactId)
        [jsonObject setObject: self.contactId forKey: @"contactId"];
    if(self.entryType)
        [jsonObject setObject: self.entryType forKey: @"entryType"];
    if(self.entryValue)
        [jsonObject setObject: self.entryValue forKey: @"entryValue"];
    if(self.creatorUid)
        [jsonObject setObject: self.creatorUid forKey: @"creatorUid"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.contactId = [jsonObject objectForKey: @"contactId"];
        if(self.contactId && [self.contactId isEqual:[NSNull null]])
            self.contactId = nil;

        self.entryType = [jsonObject objectForKey: @"entryType"];
        if(self.entryType && [self.entryType isEqual:[NSNull null]])
            self.entryType = nil;

        self.entryValue = [jsonObject objectForKey: @"entryValue"];
        if(self.entryValue && [self.entryValue isEqual:[NSNull null]])
            self.entryValue = nil;

        self.creatorUid = [jsonObject objectForKey: @"creatorUid"];
        if(self.creatorUid && [self.creatorUid isEqual:[NSNull null]])
            self.creatorUid = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
