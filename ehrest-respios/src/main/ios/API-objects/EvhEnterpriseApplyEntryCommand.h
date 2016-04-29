//
// EvhEnterpriseApplyEntryCommand.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:56 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEnterpriseApplyEntryCommand
//
@interface EvhEnterpriseApplyEntryCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* sourceId;

@property(nonatomic, copy) NSString* sourceType;

@property(nonatomic, copy) NSString* enterpriseName;

@property(nonatomic, copy) NSNumber* enterpriseId;

@property(nonatomic, copy) NSString* applyUserName;

@property(nonatomic, copy) NSString* contactPhone;

@property(nonatomic, copy) NSNumber* applyType;

@property(nonatomic, copy) NSNumber* sizeUnit;

@property(nonatomic, copy) NSNumber* areaSize;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSString* description_;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

