//
// EvhVideoConfAccountStatisticsDTO.h
// generated at 2016-04-07 10:47:32 
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

