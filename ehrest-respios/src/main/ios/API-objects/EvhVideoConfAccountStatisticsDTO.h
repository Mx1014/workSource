//
// EvhVideoConfAccountStatisticsDTO.h
// generated at 2016-04-08 20:09:21 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVideoConfAccountStatisticsDTO
//
@interface EvhVideoConfAccountStatisticsDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* confAccounts;

@property(nonatomic, copy) NSNumber* validConfAccount;

@property(nonatomic, copy) NSNumber* theNewConfAccount;

@property(nonatomic, copy) NSString* confType;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

