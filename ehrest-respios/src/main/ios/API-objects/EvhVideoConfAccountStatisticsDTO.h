//
// EvhVideoConfAccountStatisticsDTO.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
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

