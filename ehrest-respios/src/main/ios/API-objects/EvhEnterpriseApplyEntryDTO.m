//
// EvhEnterpriseApplyEntryDTO.m
//
#import "EvhEnterpriseApplyEntryDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEnterpriseApplyEntryDTO
//

@implementation EvhEnterpriseApplyEntryDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhEnterpriseApplyEntryDTO* obj = [EvhEnterpriseApplyEntryDTO new];
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
    if(self.sourceType)
        [jsonObject setObject: self.sourceType forKey: @"sourceType"];
    if(self.sourceId)
        [jsonObject setObject: self.sourceId forKey: @"sourceId"];
    if(self.enterpriseName)
        [jsonObject setObject: self.enterpriseName forKey: @"enterpriseName"];
    if(self.enterpriseId)
        [jsonObject setObject: self.enterpriseId forKey: @"enterpriseId"];
    if(self.applyContact)
        [jsonObject setObject: self.applyContact forKey: @"applyContact"];
    if(self.applyUserId)
        [jsonObject setObject: self.applyUserId forKey: @"applyUserId"];
    if(self.applyUserName)
        [jsonObject setObject: self.applyUserName forKey: @"applyUserName"];
    if(self.applyType)
        [jsonObject setObject: self.applyType forKey: @"applyType"];
    if(self.sizeUnit)
        [jsonObject setObject: self.sizeUnit forKey: @"sizeUnit"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.areaSize)
        [jsonObject setObject: self.areaSize forKey: @"areaSize"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.behaviorTime)
        [jsonObject setObject: self.behaviorTime forKey: @"behaviorTime"];
    if(self.sourceName)
        [jsonObject setObject: self.sourceName forKey: @"sourceName"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.sourceType = [jsonObject objectForKey: @"sourceType"];
        if(self.sourceType && [self.sourceType isEqual:[NSNull null]])
            self.sourceType = nil;

        self.sourceId = [jsonObject objectForKey: @"sourceId"];
        if(self.sourceId && [self.sourceId isEqual:[NSNull null]])
            self.sourceId = nil;

        self.enterpriseName = [jsonObject objectForKey: @"enterpriseName"];
        if(self.enterpriseName && [self.enterpriseName isEqual:[NSNull null]])
            self.enterpriseName = nil;

        self.enterpriseId = [jsonObject objectForKey: @"enterpriseId"];
        if(self.enterpriseId && [self.enterpriseId isEqual:[NSNull null]])
            self.enterpriseId = nil;

        self.applyContact = [jsonObject objectForKey: @"applyContact"];
        if(self.applyContact && [self.applyContact isEqual:[NSNull null]])
            self.applyContact = nil;

        self.applyUserId = [jsonObject objectForKey: @"applyUserId"];
        if(self.applyUserId && [self.applyUserId isEqual:[NSNull null]])
            self.applyUserId = nil;

        self.applyUserName = [jsonObject objectForKey: @"applyUserName"];
        if(self.applyUserName && [self.applyUserName isEqual:[NSNull null]])
            self.applyUserName = nil;

        self.applyType = [jsonObject objectForKey: @"applyType"];
        if(self.applyType && [self.applyType isEqual:[NSNull null]])
            self.applyType = nil;

        self.sizeUnit = [jsonObject objectForKey: @"sizeUnit"];
        if(self.sizeUnit && [self.sizeUnit isEqual:[NSNull null]])
            self.sizeUnit = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.areaSize = [jsonObject objectForKey: @"areaSize"];
        if(self.areaSize && [self.areaSize isEqual:[NSNull null]])
            self.areaSize = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.behaviorTime = [jsonObject objectForKey: @"behaviorTime"];
        if(self.behaviorTime && [self.behaviorTime isEqual:[NSNull null]])
            self.behaviorTime = nil;

        self.sourceName = [jsonObject objectForKey: @"sourceName"];
        if(self.sourceName && [self.sourceName isEqual:[NSNull null]])
            self.sourceName = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
