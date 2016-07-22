//
// EvhUpdateEnterpriseCommand.m
//
#import "EvhUpdateEnterpriseCommand.h"
#import "EvhOrganizationAddressDTO.h"
#import "EvhForumAttachmentDescriptor.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateEnterpriseCommand
//

@implementation EvhUpdateEnterpriseCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUpdateEnterpriseCommand* obj = [EvhUpdateEnterpriseCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _addressDTOs = [NSMutableArray new];
        _attachments = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.displayName)
        [jsonObject setObject: self.displayName forKey: @"displayName"];
    if(self.avatar)
        [jsonObject setObject: self.avatar forKey: @"avatar"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.memberCount)
        [jsonObject setObject: self.memberCount forKey: @"memberCount"];
    if(self.contactsPhone)
        [jsonObject setObject: self.contactsPhone forKey: @"contactsPhone"];
    if(self.contactor)
        [jsonObject setObject: self.contactor forKey: @"contactor"];
    if(self.entries)
        [jsonObject setObject: self.entries forKey: @"entries"];
    if(self.address)
        [jsonObject setObject: self.address forKey: @"address"];
    if(self.checkinDate)
        [jsonObject setObject: self.checkinDate forKey: @"checkinDate"];
    if(self.postUri)
        [jsonObject setObject: self.postUri forKey: @"postUri"];
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
    if(self.addressDTOs) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhOrganizationAddressDTO* item in self.addressDTOs) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"addressDTOs"];
    }
    if(self.attachments) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhForumAttachmentDescriptor* item in self.attachments) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"attachments"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.displayName = [jsonObject objectForKey: @"displayName"];
        if(self.displayName && [self.displayName isEqual:[NSNull null]])
            self.displayName = nil;

        self.avatar = [jsonObject objectForKey: @"avatar"];
        if(self.avatar && [self.avatar isEqual:[NSNull null]])
            self.avatar = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.memberCount = [jsonObject objectForKey: @"memberCount"];
        if(self.memberCount && [self.memberCount isEqual:[NSNull null]])
            self.memberCount = nil;

        self.contactsPhone = [jsonObject objectForKey: @"contactsPhone"];
        if(self.contactsPhone && [self.contactsPhone isEqual:[NSNull null]])
            self.contactsPhone = nil;

        self.contactor = [jsonObject objectForKey: @"contactor"];
        if(self.contactor && [self.contactor isEqual:[NSNull null]])
            self.contactor = nil;

        self.entries = [jsonObject objectForKey: @"entries"];
        if(self.entries && [self.entries isEqual:[NSNull null]])
            self.entries = nil;

        self.address = [jsonObject objectForKey: @"address"];
        if(self.address && [self.address isEqual:[NSNull null]])
            self.address = nil;

        self.checkinDate = [jsonObject objectForKey: @"checkinDate"];
        if(self.checkinDate && [self.checkinDate isEqual:[NSNull null]])
            self.checkinDate = nil;

        self.postUri = [jsonObject objectForKey: @"postUri"];
        if(self.postUri && [self.postUri isEqual:[NSNull null]])
            self.postUri = nil;

        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"addressDTOs"];
            for(id itemJson in jsonArray) {
                EvhOrganizationAddressDTO* item = [EvhOrganizationAddressDTO new];
                
                [item fromJson: itemJson];
                [self.addressDTOs addObject: item];
            }
        }
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"attachments"];
            for(id itemJson in jsonArray) {
                EvhForumAttachmentDescriptor* item = [EvhForumAttachmentDescriptor new];
                
                [item fromJson: itemJson];
                [self.attachments addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
