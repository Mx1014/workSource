//
// EvhListPunchExceptionRequestCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPunchExceptionRequestCommand
//
@interface EvhListPunchExceptionRequestCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* enterpriseId;

@property(nonatomic, copy) NSString* keyword;

@property(nonatomic, copy) NSString* startDay;

@property(nonatomic, copy) NSString* endDay;

@property(nonatomic, copy) NSNumber* exceptionStatus;

@property(nonatomic, copy) NSNumber* processCode;

@property(nonatomic, copy) NSNumber* pageOffset;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

