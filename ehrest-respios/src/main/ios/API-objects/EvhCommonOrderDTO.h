//
// EvhCommonOrderDTO.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:55 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommonOrderDTO
//
@interface EvhCommonOrderDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* orderNo;

@property(nonatomic, copy) NSString* orderType;

@property(nonatomic, copy) NSNumber* totalFee;

@property(nonatomic, copy) NSString* subject;

@property(nonatomic, copy) NSString* body;

@property(nonatomic, copy) NSString* appKey;

@property(nonatomic, copy) NSString* signature;

@property(nonatomic, copy) NSNumber* timestamp;

@property(nonatomic, copy) NSNumber* randomNum;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

